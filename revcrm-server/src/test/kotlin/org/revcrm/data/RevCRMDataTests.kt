package org.revcrm.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class RevCRMDataTests {

    init {
        println("started...")
    }

    @Nested
    inner class GetEntityMetadata {

        @Test
        fun `it works`() {
            assertThat(1).isEqualTo(1)
        }

        @Test
        fun `it really does!`() {
            assertThat(1).isEqualTo(1)
        }
    }
}
