package org.revcrm.models.tests

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object AccountTest: Spek({
    describe("Account model") {
        it("works beautifully") {
            assertThat(1).isEqualTo(1)
        }
    }
})