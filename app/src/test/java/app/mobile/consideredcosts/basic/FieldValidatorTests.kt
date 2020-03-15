package app.mobile.consideredcosts.basic

import org.junit.Assert
import org.junit.Test

class FieldValidatorTests {
    private val fieldValidator = FieldValidator()

    // Correct username validation
    @Test
    fun validUsername() {
        // Cool username
        Assert.assertTrue(fieldValidator.isUsernameValid("trugaaa"))
        // Username with A-Za-Z0-9
        Assert.assertTrue(fieldValidator.isUsernameValid("T3ugaaa"))
        // Username with "_"
        Assert.assertTrue(fieldValidator.isUsernameValid("True_Gamer"))
        // Username with dot
        Assert.assertTrue(fieldValidator.isUsernameValid("True.Gamer"))
        // Username with "-"
        Assert.assertTrue(fieldValidator.isUsernameValid("True-Gamer"))
    }

    // Incorrect username validation
    @Test
    fun invalidUsername() {
        // <3 symbols username
        Assert.assertFalse(fieldValidator.isUsernameValid("tr"))
        // >64 symbols username
        Assert.assertFalse(fieldValidator.isUsernameValid("jbqqhbdnuilyldpkbjkjuoafiwrdxzggsywedtlmcfrcfihikrmawhtbausjbfdfa"))
        // Unsupported special chars
        Assert.assertFalse(fieldValidator.isUsernameValid("True*Gamer"))
        Assert.assertFalse(fieldValidator.isUsernameValid("True^Gamer"))
        Assert.assertFalse(fieldValidator.isUsernameValid("True\$Gamer"))
        // Cyrillic symbols
        Assert.assertFalse(fieldValidator.isUsernameValid("iАндрюхаI"))
    }


    // Correct email validation
    @Test
    fun validEmail() {
        // Common email
        Assert.assertTrue(fieldValidator.isEmailValid("andrey.trugaaa@gmail.com"))
        // Common email with digits
        Assert.assertTrue(fieldValidator.isEmailValid("gord34326@gmail.com"))
        // Email with "_"
        Assert.assertTrue(fieldValidator.isEmailValid("gord_34326@gmail.com"))
        // Email with dot
        Assert.assertTrue(fieldValidator.isEmailValid("gord.34326@gmail.com"))
        // Email with "-" i domain part
        Assert.assertTrue(fieldValidator.isEmailValid("gord.34326@gma-il.com"))
    }

    // Incorrect email validation
    @Test
    fun invalidEmail() {
        // Without @
        Assert.assertFalse(fieldValidator.isEmailValid("andrey.trugaaagmail.com"))
        // <3 symbols local part
        Assert.assertFalse(fieldValidator.isEmailValid("ad@gmail.com"))
        //  <3 symbols domain part
        Assert.assertFalse(fieldValidator.isEmailValid("andrey.trugaa@a.a"))
        // Local part two dots in row
        Assert.assertFalse(fieldValidator.isEmailValid("andrey..trugaa@gmail.com"))
        // Domain part two dots in row
        Assert.assertFalse(fieldValidator.isEmailValid("andrey.trugaa@gmail..com"))
        // Domain part "-" in row
        Assert.assertFalse(fieldValidator.isEmailValid("andrey.trugaa@gm--ail.com"))
        // Two @
        Assert.assertFalse(fieldValidator.isEmailValid("andrey.trug@aa@mail.com"))
        // Cyrillic symbols
        Assert.assertFalse(fieldValidator.isEmailValid("anдреy.trugaa@mail.com"))

    }

    // Correct password validation
    @Test
    fun validPassword() {
        //Just all necessary symbols
        Assert.assertTrue(fieldValidator.isPasswordValid("31AAa1dzZ"))
        //All necessary symbols and some special chars
        Assert.assertTrue(fieldValidator.isPasswordValid("31@A%Aa1dz#Z"))
    }

    // Incorrect password validation
    @Test
    fun invalidPassword() {
        //No digit
        Assert.assertFalse(fieldValidator.isPasswordValid("AAadzZ"))
        //No upper-case symbol
        Assert.assertFalse(fieldValidator.isPasswordValid("adada131"))
        //No lower-case symbol
        Assert.assertFalse(fieldValidator.isPasswordValid("1ADADADA313A"))
        //All symbols correct but <8 symbols length
        Assert.assertFalse(fieldValidator.isPasswordValid("31AdZ"))
        //All symbols correct but >255 symbols length
        Assert.assertFalse(fieldValidator.isPasswordValid("IHQucre9i1bYHW72hkJFCCXEf6Ryb66UFbSd20vKM7elabL7cv1UGbJefGjoSgNL7W7DbPaKZrUMq7LZlZ8qYSAKgpzfyPFE1Tod6OGvPHP9pKoXyL3vw7oKsdHo9VrAIhO0p3iBe4jNocbvdRPi2TMO66Xoi24bjjhqlumsw3lDWxNxDyma6jOyQFJA16NP523tYJEz2yfHjjtZei21BCdFr4A9X2vofQ5ho1Oou2UPnUdN1qF7rE7ihD5gGj9A"))
    }
}
