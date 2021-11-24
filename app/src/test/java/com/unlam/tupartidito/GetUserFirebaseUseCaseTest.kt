package com.unlam.tupartidito


import com.unlam.tupartidito.data.UserRepository
import com.unlam.tupartidito.data.model.user.User
import com.unlam.tupartidito.data.network.ClubFirebaseDatabase
import com.unlam.tupartidito.data.network.UserFirebaseDatabase
import com.unlam.tupartidito.domain.user.GetUserFirebaseUseCase
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetUserFirebaseUseCaseTest  {


    @MockK
    lateinit var repository: UserRepository
    lateinit var instanceUseCase: GetUserFirebaseUseCase



    @Before
     fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        instanceUseCase = GetUserFirebaseUseCase(repository)


    }

    @Test
     fun `test que prueba que trae el usuario correcto`() = runBlockingTest{

       val userExpected = User("nico", "Nicolas","1234", ArrayList())
        coEvery { repository.getUser("nico") } returns userExpected

        assertThat(instanceUseCase("nico") == userExpected).isTrue
        coVerify(exactly = 1) { repository.getUser("nico") }
    }


    @Test
    fun `test que prueba que trae null cuando el usuario no existe`() = runBlockingTest{

        val userExpected = null
        coEvery { repository.getUser("pedro") }returns userExpected

        assertThat(instanceUseCase("pedro") == userExpected).isTrue
        coVerify(exactly = 1) { repository.getUser("pedro") }
    }



}
