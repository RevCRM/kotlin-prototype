
import * as React from 'react';
import { ViewManager } from 'revcrm/lib/client';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';

interface IInvoiceLine {
    item: string;
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
    items: IInvoiceLine[];
    invoice_net_total: string;
    invoice_tax: string;
}

const inv: IInvoice = require('../../invoice_data.json');

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

    console.log('registered!!');

 }
