package org.example;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class gatosTest {

    @Test
    public void testCriarGato() {
        gatos gatos = new gatos();
        Gato gato = new Gato();

        gato.setNome("gatoCr");
        gato.setIdade(2);
        gato.setCastrado(false);
        gato.setSexo("Macho");

        Response response = gatos.criarGato(gato);

        assertEquals(Response.Status.CREATED.getStatusCode(), ((Response) response).getStatus());
    }

    @Test
    public void testListarGatos() {
        gatos gatos = new gatos();

        Response response = gatos.listarGatos();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testBuscarGatoPorId() {
        gatos gatos = new gatos();

        Response response = gatos.buscarGatoPorId(1);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void testAtualizarGato() {
        gatos gatos = new gatos();
        Gato gato = new Gato();

        gato.setNome("gatoAtt");
        gato.setIdade(2);
        gato.setCastrado(true);
        gato.setSexo("Macho");

        // Criar um gato inicialmente
        Response createResponse = gatos.criarGato(gato);
        assertEquals(Response.Status.CREATED.getStatusCode(), createResponse.getStatus());

        // Obter o ID do gato criado
        int generatedId = Integer.parseInt(createResponse.getEntity().toString());

        // Atualizar o gato recém-criado
        gato.setNome("NovoNome");
        Response updateResponse = gatos.atualizarGato(generatedId, gato);

        assertEquals(Response.Status.OK.getStatusCode(), updateResponse.getStatus());
    }

    @Test
    public void testDeletarGato() {
        gatos gatos = new gatos();
        Gato gato = new Gato();

        gato.setNome("gatoADel");
        gato.setIdade(2);
        gato.setCastrado(true);
        gato.setSexo("Macho");

        // Criar um gato inicialmente
        Response createResponse = gatos.criarGato(gato);
        assertEquals(Response.Status.CREATED.getStatusCode(), createResponse.getStatus());

        // Obter o ID do gato criado
        int generatedId = Integer.parseInt(createResponse.getEntity().toString());

        // Deletar o gato recém-criado
        Response deleteResponse = gatos.deletarGato(generatedId);

        assertEquals(Response.Status.OK.getStatusCode(), deleteResponse.getStatus());
    }
}