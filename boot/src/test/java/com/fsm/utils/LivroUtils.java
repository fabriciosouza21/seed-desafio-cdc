package com.fsm.utils;

import com.fsm.livraria.livro.controller.LivroCreateController;
import com.fsm.livraria.livro.dto.LivroCreatedRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.apache.http.HttpHeaders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Singleton
public class LivroUtils {
    private UUID livroId;

    private final RequestSpecification spec;

    private final AutenticationUtils autenticationUtils;

    private final AutorUtils autorUtils;

    private final CategoriaUtils categoriaUtils;


    public LivroUtils(RequestSpecification spec, AutenticationUtils autenticationUtils, AutorUtils autorUtils, CategoriaUtils categoriaUtils) {
        this.spec = spec;
        this.autenticationUtils = autenticationUtils;
        this.autorUtils = autorUtils;
        this.categoriaUtils = categoriaUtils;
    }

    public UUID getLivro() {
        return getLivro(true);
    }


    public UUID getLivro(boolean cache){

        if (livroId != null && cache) {
            return livroId;
        }

        // Obter token
        String token = autenticationUtils.getToken();

        // Criar categoria
        UUID getCategoryId = categoriaUtils.getCategoria();

        // Criar autor
        UUID getAutorId = autorUtils.getAutor();

        // Criar livro
        LivroCreatedRequest livroRequest = createValidLivroRequest(getAutorId, getCategoryId);

        // Enviar requisição
        Response post = spec.given().contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(livroRequest)
                .when()
                .post(LivroCreateController.LIVRO_CREATED);
        post
                .then()
                .statusCode(201);





            post
                    .then()
                    .statusCode(201);


        livroId = extrairId(post);

        return livroId;
    }

    private UUID extrairId(Response response) {

        String locationHeader = response.then()
                .extract()
                .header("Location");
        // Extrair ID do autor da URL de localização
        String[] parts = locationHeader.split("/");

        return UUID.fromString(parts[parts.length - 1]);
    }

    private LivroCreatedRequest createValidLivroRequest(@NotNull UUID getAutorId, @NotNull UUID getCategoryId) {
        String uniqueId = String.valueOf(System.currentTimeMillis());

        // Garantir que os UUIDs sejam válidos
        String getAutorIdStr = getAutorId != null ? getAutorId.toString() : null;
        String getCategoryIdStr = getCategoryId != null ? getCategoryId.toString() : null;

        // Criar o objeto com todos os campos corretamente formatados
        return new LivroCreatedRequest(
                "Livro Teste " + uniqueId,         // getTitle
                "Resumo do livro teste",           // getResume
                "Sumário detalhado do livro",      // getSummary (pode ser null, certifique-se que seja válido)
                "getIsbn-" + uniqueId,                // getIsbn
                200,                               // getNumberPages
                LocalDateTime.now().plusMonths(1), // getPublicationDate
                new BigDecimal("50"),           // getPrice
                getAutorIdStr,                           // getAutorId
                getCategoryIdStr                       // getCategoryId
        );
    }
}
