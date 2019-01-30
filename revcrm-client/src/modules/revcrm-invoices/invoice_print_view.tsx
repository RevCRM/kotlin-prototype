import * as React from "react"
import { ViewHeaderBar } from "../../components/views/widgets/ViewHeaderBar"
import gql from "graphql-tag"
import { Query } from "react-apollo"
import { IEntityQueryResults } from "../../graphql/queries"
import { IViewManagerContextProp, withViewManagerContext } from "../../components/views/ViewManager"
import {
    createStyles,
    Divider,
    Grid,
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow,
    Theme,
    Typography, WithStyles, withStyles
} from "@material-ui/core"

const INVOICE_QUERY = gql`
    query ($id: String!) {
        Invoice ( where: { id: $id }) {
            results {
                id
                account {
                    id
                    company_name
                    title
                    first_name
                    last_name
                    primary_address {
                        full_address
                    }
                }
                invoice_number
                invoice_date
                payment_due_date
                invoice_currency
                lines {
                    item
                    quantity
                    unit
                    unit_price
                    net_total
                    line_tax
                }
                invoice_net_total
                invoice_tax_total
                invoice_total
            }
        }
        Company {
            results {
                name
                tagline
                tax_id
                payment_information
            }
        }
    }
`

export const styles = (theme: Theme) => createStyles({
    invoiceWrapper: {
        margin: "16px auto",
        maxWidth: 1024,
        padding: 16,
        boxShadow: "0px 1px 3px 0px rgba(0,0,0,0.2),0px 1px 1px 0px rgba(0,0,0,0.14),0px 2px 1px -1px rgba(0,0,0,0.12)",
        borderRadius: 4,
        backgroundColor: "#fff"
    },
    tableCell: {
        padding: 0
    },
    temp: {
        "@media print": {
            maxWidth: "none"
        }
    }
})

export interface IInvoicePrintProps extends
    IViewManagerContextProp,
    WithStyles<typeof styles> {
}

// TODO: Number formatting should come from settings + an intl library
const toMoney = (num: any) =>
    Number(num).toLocaleString(undefined, {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    })

export const InvoicePrint = withStyles(styles)(withViewManagerContext((props: IInvoicePrintProps) => {

    const { classes } = props

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
                    if (!data || !data.Invoice || !data.Invoice.results) return "No data returned"
                    if (!data.Company || !data.Company.results) return "No company information"

                    const invoice = data.Invoice.results[0]
                    const company = data.Company.results[0]

                    const { account } = invoice

                    const invoiceTo = `${account.title} ${account.first_name} ${account.last_name}`
                        + `\n${account.company_name}`
                        + `\n${account.primary_address.full_address}`

                    return (
                        <div className={classes.invoiceWrapper}>
                            <Grid container spacing={8}>
                                <Grid item xs={12}>
                                    <Typography variant="display3">{company.name}</Typography>
                                    <Typography variant="subheading">{company.tagline}</Typography>

                                    <Typography variant="headline" style={{marginTop: 30, marginBottom: 30, textAlign: "center"}}>TAX INVOICE</Typography>
                                </Grid>
                                <Grid item xs={6}>
                                    <Typography variant="body1"><b>To:</b></Typography>
                                    <Typography variant="body1" style={{ whiteSpace: "pre-line"}}>{invoiceTo}</Typography>
                                </Grid>
                                <Grid item xs={6} container>
                                    <Grid item xs={6}><Typography variant="body1"><b>GST Number:</b></Typography></Grid>
                                    <Grid item xs={6}><Typography variant="body1">{company.tax_id}</Typography></Grid>

                                    <Grid item xs={6}><Typography variant="body1"><b>Invoice Date:</b></Typography></Grid>
                                    <Grid item xs={6}><Typography variant="body1">{invoice.invoice_date}</Typography></Grid>

                                    <Grid item xs={6}><Typography variant="body1"><b>Invoice Number:</b></Typography></Grid>
                                    <Grid item xs={6}><Typography variant="body1">{invoice.invoice_number}</Typography></Grid>
                                </Grid>
                                <Grid item xs={12}>
                                    <Table>
                                        <TableHead>
                                            <TableRow>
                                                <TableCell className={classes.tableCell}>Item</TableCell>
                                                <TableCell className={classes.tableCell} align="right">Quantity</TableCell>
                                                <TableCell className={classes.tableCell} align="right">Unit</TableCell>
                                                <TableCell className={classes.tableCell} align="right">Unit Price ({invoice.invoice_currency})</TableCell>
                                                <TableCell className={classes.tableCell} align="right">Total (excl. GST)</TableCell>
                                                <TableCell className={classes.tableCell} align="right">GST @ 15%</TableCell>
                                            </TableRow>
                                        </TableHead>
                                        <TableBody>
                                            {invoice.lines.map((item: any, idx: number) => (
                                                <TableRow key={idx}>
                                                    <TableCell className={classes.tableCell}>{item.item}</TableCell>
                                                    <TableCell className={classes.tableCell} align="right">{item.quantity}</TableCell>
                                                    <TableCell className={classes.tableCell} align="right">{item.unit}</TableCell>
                                                    <TableCell className={classes.tableCell} align="right">{toMoney(item.unit_price)}</TableCell>
                                                    <TableCell className={classes.tableCell} align="right">{toMoney(item.net_total)}</TableCell>
                                                    <TableCell className={classes.tableCell} align="right">{toMoney(item.line_tax)}</TableCell>
                                                </TableRow>
                                            ))}
                                        </TableBody>
                                    </Table>
                                </Grid>
                                <Grid item xs={6} />
                                <Grid item xs={6} container>
                                    <Grid item xs={12} style={{height: 20}} />
                                    <Grid item xs={6}><Typography variant="body1" align="right"><b>Total (excl. GST):</b></Typography></Grid>
                                    <Grid item xs={6}><Typography variant="body1" align="right">{toMoney(invoice.invoice_net_total)} {invoice.invoice_currency}</Typography></Grid>
                                    <Grid item xs={12} style={{height: 10}} />
                                    <Grid item xs={6}><Typography variant="body1" align="right"><b>GST:</b></Typography></Grid>
                                    <Grid item xs={6}><Typography variant="body1" align="right">{toMoney(invoice.invoice_tax_total)} {invoice.invoice_currency}</Typography></Grid>
                                    <Grid item xs={12} style={{height: 10}} />
                                    <Grid item xs={6}><Typography variant="body1" align="right"><b>Invoice Total:</b></Typography></Grid>
                                    <Grid item xs={6}><Typography variant="body1" align="right"><b>{toMoney(invoice.invoice_total)} {invoice.invoice_currency}</b></Typography></Grid>
                                    <Grid item xs={12} style={{height: 10}} />
                                    <Grid item xs={6}><Typography variant="body1" align="right"><b>Payment Due:</b></Typography></Grid>
                                    <Grid item xs={6}><Typography variant="body1" align="right"><b>{invoice.payment_due_date}</b></Typography></Grid>
                                    <Grid item xs={12} style={{height: 20}} />
                                </Grid>
                                <Grid item xs={12}>
                                    <Divider />
                                    <Grid item xs={12} style={{height: 20}} />
                                    <Typography variant="title" style={{ marginBottom: 12 }}>
                                        Payment Information
                                    </Typography>
                                    <Typography variant="body1">
                                        {company.payment_information}
                                    </Typography>
                                </Grid>
                            </Grid>
                        </div>
                    )
                }}
            </Query>
        </div>
    )
}))
