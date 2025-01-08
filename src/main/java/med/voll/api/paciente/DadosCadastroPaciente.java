package med.voll.api.paciente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.endereco.DadosEndereco;
import med.voll.api.medico.Especialidade;

public record DadosCadastroPaciente(
        @NotBlank
        String nome,

        @NotBlank @Email
        String email,

        @NotBlank @Pattern(regexp = "\\d{11}")
        String cpf,

        @NotNull
        String telefone,

        @NotNull @Valid
        DadosEndereco endereco) {



}
