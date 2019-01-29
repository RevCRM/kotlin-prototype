import * as React from "react"
import { ViewHeaderBar } from "../../components/views/widgets/ViewHeaderBar"

export const InvoicePrint = () => (
    <div>
        <ViewHeaderBar
            backButtonEnabled={true}
            title="Print Invoice"
        />
        <h2>printy printy!</h2>
    </div>
)
