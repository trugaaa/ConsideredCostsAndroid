package app.mobile.consideredcosts.basic

import org.junit.Assert
import org.junit.Test

class FieldValidatorTests {
    private val fieldValidator = FieldValidator()

    @Test
    fun validPassword()
    {
        Assert.assertTrue(fieldValidator.isPasswordValid("31AAa1dzZ"))
    }

    @Test
    fun validPasswordWithSpecialChars()
    {
        Assert.assertTrue(fieldValidator.isPasswordValid("31@A%Aa1dz#Z"))
    }

    @Test
    fun invalidPasswordWitoutDigit()
    {
        Assert.assertFalse(fieldValidator.isPasswordValid("AAadzZ"))
    }

    @Test
    fun invalidPasswordWitoutUpperLetter()
    {
        Assert.assertFalse(fieldValidator.isPasswordValid("adada131"))
    }

    @Test
    fun invalidPasswordWitoutLowerLetter()
    {
        Assert.assertFalse(fieldValidator.isPasswordValid("1ADADADA313A"))
    }
}
