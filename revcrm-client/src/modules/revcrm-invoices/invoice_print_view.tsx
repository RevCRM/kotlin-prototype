import * as React from "react"
import { ViewHeaderBar } from "../../components/views/widgets/ViewHeaderBar"
import gql from "graphql-tag"
import { Query } from "react-apollo"
import { IEntityQueryResults } from "../../graphql/queries"
import { withViewManagerContext } from "../../components/views/ViewManager"

const INVOICE_QUERY = gql`
    query ($id: String!) {
        Invoice ( where: { id: $id }) {
            results {
                id
                account {
                    id
                    record_name
                }
                invoice_number
                invoice_date
                invoice_currency
                lines {
                    item
                    net_total
                    line_tax
                    line_total
                }
            }
        }
    }
`

export const InvoicePrint = withViewManagerContext((props) => {

    return (
        <div>
            <ViewHeaderBar
                backButtonEnabled={true}
                title="Print Invoice"
            />
            <Query<IEntityQueryResults>
                query={INVOICE_QUERY}
                variables={{
                    id: props.view.context.id
                }}
            >
                {({ loading, error, data }) => {

                    if (loading) return null
                    if (error) return `Error! ${error.message}`
                    if (!data) return "No data returned"

                    const invoice = data.Invoice.results[0]

                    return (
                        <h2>Account: {invoice.account.record_name}, Invoice: {invoice.invoice_number}</h2>
                    )
                }}
            </Query>
        </div>
    )
})
