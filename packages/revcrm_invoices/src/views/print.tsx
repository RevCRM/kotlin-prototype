
import * as React from 'react';
import { ViewManager } from 'revcrm/lib/client';

export function registerViews(views: ViewManager) {

    // Account views
    views.registerView({
        name: 'invoice_print',
        model: 'Account',
        component: (
            <div>I am an invoice!</div>
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
