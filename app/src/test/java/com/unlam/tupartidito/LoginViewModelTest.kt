package com.unlam.tupartidito

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.unlam.tupartidito.data.model.user.User
import com.unlam.tupartidito.domain.user.*
import com.unlam.tupartidito.ui.login.LoginViewModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test



@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var getUserFirebaseUseCase: GetUserFirebaseUseCase
    private lateinit var instanceLoginViewModel: LoginViewModel
    private lateinit var preferences: SharedPreferences

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        preferences = mockk()
        instanceLoginViewModel = LoginViewModel(
            getUserFirebaseUseCase,
            CredentialsNotEmptyUseCase(),
            ValidUserUseCase(),
            ValidCredentialsUseCase(),
            RememberUserUseCase(),
        )

    }


    @Test
    fun `test que prueba que trae el usuario correcto`() = runBlockingTest {

        val userExpected = User("nico", "Nicolas", "1234", ArrayList())
        coEvery { getUserFirebaseUseCase("nico") } returns User(
            "nico",
            "Nicolas",
            "1234",
            ArrayList()
        )
        every { preferences.getBoolean("active", false) } returns true


        instanceLoginViewModel.loginSession("nico", "1234", preferences)


        assertThat(instanceLoginViewModel.userData.value != null).isTrue
        assertThat(instanceLoginViewModel.userData.value?.user == userExpected).isTrue
        coVerify(exactly = 1) { getUserFirebaseUseCase("nico") }
    }

    @Test
    fun `test que prueba que trae null cuando el usuario no existe`() = runBlockingTest {


        coEvery { getUserFirebaseUseCase("pedro") } returns null
        every { preferences.getBoolean("active", false) } returns true


        instanceLoginViewModel.loginSession("pedro", "1234", preferences)


        assertThat(instanceLoginViewModel.userData.value?.user == null).isTrue
        coVerify(exactly = 1) { getUserFirebaseUseCase("pedro") }
    }


    @After
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }


}