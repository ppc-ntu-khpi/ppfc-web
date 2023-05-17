/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package api

class AuthenticationException : ApiException(message = "Incorrect username or password.")