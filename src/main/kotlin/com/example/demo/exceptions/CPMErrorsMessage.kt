package com.example.demo.exceptions

enum class CPMErrorsMessage(private val codeError: String = "", private val message : String): CPMError {

    USERNAME_ERROR("USERNAME_ERROR", "Le nom utilisateur est trop court il doit avoir plus de 5 caracteres"),
    PASSWORD_ERROR("PASSWORD_ERROR", "Le mot de passe est trop court il doit avoir plus de 8 caracteres"),
    LOGIN_ERROR("LOGIN_ERROR", "Le nom d'utilisateur ou le mot de passe fournie n'est pas bon"),
    USER_NOT_FOUND("USER_NOT_FOUND", "L'utilisateur n'est pas trouve"),
    USER_ALREADY_EXIST("USER_ALREADY_EXIST","Le nom d'utilisateur est deja utilise"),
    DEPARTMENT_NOT_FOUND("DEPARTMENT_NOT_FOUND", "Ce departement n'est pas trouve"),;

    override fun getCodeError(): String? {
        return codeError;
    }

    override fun getMessage(): String? {
        return  message;
    }

}