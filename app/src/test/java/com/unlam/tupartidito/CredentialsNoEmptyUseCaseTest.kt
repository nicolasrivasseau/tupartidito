package com.unlam.tupartidito

import com.unlam.tupartidito.domain.user.CredentialsNotEmptyUseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CredentialsNoEmptyUseCaseTest: TestCase() {


    @MockK
    lateinit var instanceUseCase: CredentialsNotEmptyUseCase


    @Before
    override fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        instanceUseCase = CredentialsNotEmptyUseCase()

        super.setUp()
    }

    @Test
    fun `test que prueba que da true cuando las credenciales no estan vacias `() {

            val user = "nico"
            val password = "1234"

        assertThat(instanceUseCase(user,password)).isTrue
    }

    @Test
    fun `test que prueba que da false cuando las credenciales estan vacias `() {

        val user = ""
        val password = ""

        assertThat(instanceUseCase(user,password)).isFalse
    }



}