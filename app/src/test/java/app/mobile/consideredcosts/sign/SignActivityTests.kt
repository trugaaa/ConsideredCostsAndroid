package app.mobile.consideredcosts.sign
/*
import android.content.Context
import android.content.res.Resources
import app.mobile.consideredcosts.R
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SignActivityTests {
    private val screen: SignActivity = Mockito.mock(SignActivity())

    @Mock
    lateinit var mockContext: Context
    @Mock
    lateinit var mockResources: Resources

    private val errorEmailMessage:String = "Email is invalid. Pls check your email"
    private val errorPasswordMessage:String = "Email is invalid. Pls check your email"
    private val errorPasswordNotEqualsMessage:String = "Passwords must be equal"
    private val errorUsernameMessage:String = "Username is invalid. It can contain only characters, digits and \"_\" symbol"

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        givenThatAllStringsAreMocked()
    }

    private fun givenThatAllStringsAreMocked() {
        whenever(screen.getString(R.string.errorEmailField)).thenReturn(errorEmailMessage)
        whenever(screen.getString(R.string.errorPasswordField)).thenReturn(errorPasswordMessage)
        whenever(screen.getString(R.string.errorPassesDontMatch)).thenReturn(errorPasswordNotEqualsMessage)
        whenever(screen.getString(R.string.errorUsernameField)).thenReturn(errorUsernameMessage)
        whenever(screen.resources).thenReturn(mockResources)
    }

    @Test
    fun successValidateAllFields() {
        val list: MutableList<String> =
            screen.validateEveryField("trugaaa", "andrey.trugaaa@gmail.com", "1234AaAa", "1234AaAa")
        Assert.assertTrue(list.isEmpty())
    }

    @Ignore
    fun incorrectUsername() {
        val list: MutableList<String> =
            screen.validateEveryField("tr", "andrey.trugaaa@gmail.com", "1234AaAa", "1234AaAa")
        Assert.assertTrue(list[0]==errorUsernameMessage)
    }
}
 */