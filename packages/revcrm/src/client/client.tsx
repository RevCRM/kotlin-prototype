import * as React from 'react';
import * as ReactDOM from 'react-dom';

import { Provider } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';
import { getStore } from './store/index';

import { App } from './components/App';

import { IModelManager, ModelManager } from 'rev-models';
import { ModelApiBackend } from 'rev-api-client';
import { ModelProvider } from 'rev-ui';
import { registerComponents } from 'rev-ui-materialui';
import { registerModels } from '../models/client';
import { ViewManager, viewManager } from './ViewManager';

export class RevCRMClient {
    views: ViewManager;
    models: IModelManager;

    constructor() {
        this.views = viewManager; // TODO - should not be static
        this.models = new ModelManager();
        this.models.registerBackend('default', new ModelApiBackend('/api'));
    }

    async start() {
        registerComponents();
        registerModels(this);

        console.log('registered models', this.models);

        const store = getStore();

        ReactDOM.render((
                <Provider store={store} >
                    <ModelProvider modelManager={this.models} >
                        <BrowserRouter>
                            <App />
                        </BrowserRouter>
                    </ModelProvider>
                </Provider>
            ),
            document.getElementById('app')
        );
    }
}
