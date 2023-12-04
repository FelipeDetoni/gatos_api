package org.example;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("gatos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class gatos {

    @POST
    public Response criarGato(Gato gato) {
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO gatos (nome, idade, castrado, sexo) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, gato.getNome());
                statement.setInt(2, gato.getIdade());
                statement.setBoolean(3, gato.isCastrado());
                statement.setString(4, gato.getSexo());

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int generatedId = generatedKeys.getInt(1);
                            gato.setId(generatedId);
                            return Response.status(Response.Status.CREATED).entity(generatedId).build();
                        } else {
                            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Falha ao obter o ID gerado").build();
                        }
                    }
                } else {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Falha ao cadastrar o Gato").build();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao conectar ao banco de dados").build();
        }
    }

    @GET
    public Response listarGatos() {
        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM gatos";
            List<Gato> gatos = new ArrayList<>();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Gato gato = new Gato();
                        gato.setId(resultSet.getInt("id"));
                        gato.setNome(resultSet.getString("nome"));
                        gato.setIdade(resultSet.getInt("idade"));
                        gato.setCastrado(resultSet.getBoolean("castrado"));
                        gato.setSexo(resultSet.getString("sexo"));
                        gatos.add(gato);
                    }
                }
                return Response.status(Response.Status.OK).entity(gatos).build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao conectar ao banco de dados").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarGatoPorId(@PathParam("id") int id) {
        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM gatos WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        Gato gato = new Gato();
                        gato.setId(resultSet.getInt("id"));
                        gato.setNome(resultSet.getString("nome"));
                        gato.setIdade(resultSet.getInt("idade"));
                        gato.setCastrado(resultSet.getBoolean("castrado"));
                        gato.setSexo(resultSet.getString("sexo"));
                        return Response.status(Response.Status.OK).entity(gato).build();
                    } else {
                        return Response.status(Response.Status.NOT_FOUND).entity("Gato não encontrado").build();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao conectar ao banco de dados").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizarGato(@PathParam("id") int id, Gato gato) {
        try (Connection connection = getConnection()) {
            String sql = "UPDATE gatos SET nome = ?, idade = ?, castrado = ?, sexo = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, gato.getNome());
                statement.setInt(2, gato.getIdade());
                statement.setBoolean(3, gato.isCastrado());
                statement.setString(4, gato.getSexo());
                statement.setInt(5, id);

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    return Response.status(Response.Status.OK).entity("Gato atualizado com sucesso").build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND).entity("Gato não encontrado").build();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao conectar ao banco de dados").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarGato(@PathParam("id") int id) {
        try (Connection connection = getConnection()) {
            String sql = "DELETE FROM gatos WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    return Response.status(Response.Status.OK).entity("Gato deletado com sucesso").build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND).entity("Gato não encontrado").build();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao conectar ao banco de dados").build();
        }
    }

    private static Connection getConnection() throws SQLException {
        String jdbcUrl = "jdbc:mysql://localhost:3306/gatos";
        String usuario = "root";
        String senha = "";

        return DriverManager.getConnection(jdbcUrl, usuario, senha);
    }

    private static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}