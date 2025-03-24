package med.voll.api.Infra.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarError404(){
        //String mensagem = "Entidade não encontrada. Verifique os dados e tente novamente.";
        //return new ResponseEntity<>(mensagem, HttpStatus.NOT_FOUND);

        //ou porque no 404 nao e necessario mensagem na maioria das vezes
        return ResponseEntity.notFound().build();
    }

//código omitido

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();

        return ResponseEntity.badRequest().body(erros.stream().map(DadosErrosValidacao::new).toList());

    }

    private record DadosErrosValidacao(String campo, String mensagem) {
        public DadosErrosValidacao(FieldError erro){
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}
