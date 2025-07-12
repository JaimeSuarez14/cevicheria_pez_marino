package com.proyecto.cevicheria_pez_marino.controller;

import org.springframework.web.bind.annotation.RestController;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.proyecto.cevicheria_pez_marino.dto.ListaCarrito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@RestController
public class MercadoPagoController {

    @Value("${mercadopago.access.token}")
    private String accessToken;
    
    @RequestMapping(value="api/mp", method=RequestMethod.POST)
    public String getList(@RequestBody ListaCarrito listaCarrito) {
        
        if(listaCarrito == null){
            return "error json:/";
        }

        String title = listaCarrito.getProducto().getNombre();
        String description = listaCarrito.getProducto().getDescripcion();
        int cantidad = listaCarrito.getCantidad();
        double precio = listaCarrito.getProducto().getPrecio();

        try {
            MercadoPagoConfig.setAccessToken(accessToken);
            //1. PREFERENCIA DE VENTA
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .title(title)
                .description(description)
                .quantity(cantidad)
                .unitPrice(new BigDecimal(precio))
                .currencyId("PEN")
                .build();

            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);

            //PREFERENCIA DE BACKA
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("https://jaimesuarez14.github.io/ventana-de-exito-de-la-pasarela/")
                .pending("https://github.com/JaimeSuarez14/ventana-de-exito-de-la-pasarela.git")
                .failure("https://github.com/JaimeSuarez14/ventana-de-exito-de-la-pasarela.git")
                .build();
            
                //ENSANBLE DE PREFERENCIA
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .backUrls(backUrls)
                .build();
            
            //tipo cliente para la comunicacion
            PreferenceClient client = new PreferenceClient();
            //preferencia de nuestro cliente
            Preference preference = client.create(preferenceRequest);
            //Retornamos esa preferencia a nuestro frontend
            return preference.getId();
        }catch (MPException | MPApiException e) {
            return e.toString();
        }


        
    }
    
}
