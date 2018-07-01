
import * as React from 'react';
import { ViewManager } from 'revcrm/lib/client';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import Table from '@material-ui/core/Table';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import TableCell from '@material-ui/core/TableCell';
import TableBody from '@material-ui/core/TableBody';
import { Divider } from '@material-ui/core';

interface IInvoiceLine {
    name: string;
    quantity: string;
    unit: string;
    unit_price: string;
    line_net_total: string;
    line_tax: string;
}

interface IInvoice {
    company_name: string;
    company_tagline: string;
    invoice_to: string[];
    gst_number: string;
    invoice_date: string;
    invoice_number: string;
    invoice_currency: string;
    items: IInvoiceLine[];
    invoice_net_total: string;
    invoice_tax: string;
    invoice_total: string;
    payment_due_date: string;
    payment_information: string;
}

const inv: IInvoice = require('../../invoice_data.json');
const tableCellStyle = {
    padding: 0
};

export function registerViews(views: ViewManager) {

    // Account views
    views.registerView({
        name: 'invoice_print',
        model: 'Account',
        component: (
            <Grid container spacing={8}>
                <Grid item xs={12}>
                    <Typography variant="display3">{inv.company_name}</Typography>
                    <Typography variant="subheading">{inv.company_tagline}</Typography>

                    <Typography variant="headline" style={{marginTop: 30, marginBottom: 30, textAlign: 'center'}}>TAX INVOICE</Typography>
                </Grid>
                <Grid item xs={6}>
                    <Typography variant="body1"><b>To:</b></Typography>
                    {inv.invoice_to.map((line, idx) => (
                        <Typography variant="body1" key={idx}>{line}</Typography>
                    ))}
                </Grid>
                <Grid item xs={6} container>
                    <Grid item xs={6}><Typography variant="body1"><b>GST Number:</b></Typography></Grid>
                    <Grid item xs={6}><Typography variant="body1">{inv.gst_number}</Typography></Grid>

                    <Grid item xs={6}><Typography variant="body1"><b>Invoice Date:</b></Typography></Grid>
                    <Grid item xs={6}><Typography variant="body1">{inv.invoice_date}</Typography></Grid>

                    <Grid item xs={6}><Typography variant="body1"><b>Invoice Number:</b></Typography></Grid>
                    <Grid item xs={6}><Typography variant="body1">{inv.invoice_number}</Typography></Grid>
                </Grid>
                <Grid item xs={12}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell style={tableCellStyle}>Item</TableCell>
                                <TableCell style={tableCellStyle} numeric>Quantity</TableCell>
                                <TableCell style={tableCellStyle} numeric>Unit</TableCell>
                                <TableCell style={tableCellStyle} numeric>Unit Price ({inv.invoice_currency})</TableCell>
                                <TableCell style={tableCellStyle} numeric>Total (excl. GST)</TableCell>
                                <TableCell style={tableCellStyle} numeric>GST @ 15%</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                        {inv.items.map((item, idx) => (
                            <TableRow key={idx}>
                                <TableCell style={tableCellStyle}>{item.name}</TableCell>
                                <TableCell style={tableCellStyle} numeric>{item.quantity}</TableCell>
                                <TableCell style={tableCellStyle} numeric>{item.unit}</TableCell>
                                <TableCell style={tableCellStyle} numeric>{item.unit_price}</TableCell>
                                <TableCell style={tableCellStyle} numeric>{item.line_net_total}</TableCell>
                                <TableCell style={tableCellStyle} numeric>{item.line_tax}</TableCell>
                            </TableRow>
                        ))}
                        </TableBody>
                    </Table>
                </Grid>
                <Grid item xs={6} />
                <Grid item xs={6} container>
                    <Grid item xs={12} style={{height: 20}} />
                    <Grid item xs={6}><Typography variant="body1" align="right"><b>Total (excl. GST):</b></Typography></Grid>
                    <Grid item xs={6}><Typography variant="body1" align="right">{inv.invoice_net_total} {inv.invoice_currency}</Typography></Grid>
                    <Grid item xs={12} style={{height: 10}} />
                    <Grid item xs={6}><Typography variant="body1" align="right"><b>GST:</b></Typography></Grid>
                    <Grid item xs={6}><Typography variant="body1" align="right">{inv.invoice_tax} {inv.invoice_currency}</Typography></Grid>
                    <Grid item xs={12} style={{height: 10}} />
                    <Grid item xs={6}><Typography variant="body1" align="right"><b>Invoice Total:</b></Typography></Grid>
                    <Grid item xs={6}><Typography variant="body1" align="right"><b>{inv.invoice_total} {inv.invoice_currency}</b></Typography></Grid>
                    <Grid item xs={12} style={{height: 10}} />
                    <Grid item xs={6}><Typography variant="body1" align="right"><b>Payment Due:</b></Typography></Grid>
                    <Grid item xs={6}><Typography variant="body1" align="right"><b>{inv.payment_due_date}</b></Typography></Grid>
                    <Grid item xs={12} style={{height: 20}} />
                </Grid>
                <Grid item xs={12}>
                    <Divider />
                    <Grid item xs={12} style={{height: 20}} />
                    <Typography variant="title" style={{ marginBottom: 12 }}>
                        Payment Information
                    </Typography>
                    <Typography variant="body1">
                        {inv.payment_information}
                    </Typography>
                </Grid>
            </Grid>
        )
    });

    views.registerPerspective({
        name: 'invoices',
        title: 'Invoices',
        views: {
            print: 'invoice_print'
        }
    });

 }
