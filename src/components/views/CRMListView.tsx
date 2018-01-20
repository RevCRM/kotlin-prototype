
import * as React from 'react';
import * as PropTypes from 'prop-types';

import Button from 'material-ui/Button';
import Add from 'material-ui-icons/Add';
import { ListView, IModelManagerProp } from 'rev-forms-materialui';
import { ICRMViewManagerContext } from './CRMViewManager';
import { IModel } from 'rev-models';
import { withModelManager } from 'rev-forms-materialui/lib/provider/withModelManager';

export interface ICRMListViewProps {
    fields: string[];
    detailView?: string;
}

class CRMListViewC extends React.Component<ICRMListViewProps & IModelManagerProp> {

    context: ICRMViewManagerContext;
    static contextTypes = {
        viewContext: PropTypes.object
    };

    onRecordClicked(model: IModel) {
        if (!this.props.detailView) {
            throw new Error(`CRMListView onRecordClicked() Error: no detailView set in view: ${this.context.viewContext.view.name}`);
        }

        const meta = this.props.modelManager.getModelMeta(model);
        const args = {
            [meta.primaryKey]: model[meta.primaryKey]
        };
        const [ perspectiveName, viewName ] = this.props.detailView.split('/');

        this.context.viewContext.changePerspective(perspectiveName, viewName, args);
    }

    render() {
        console.log('ListView props', this.props);
        console.log('ListView context', this.context);
        const ctx = this.context.viewContext;
        return (
            <div>
                <Button raised color="primary" style={{ marginBottom: 20 }}>
                    <Add style={{ marginRight: 10 }} />
                    New
                </Button>
                <ListView
                    model={ctx.view.model}
                    fields={this.props.fields}
                    onRecordClick={(record) => this.onRecordClicked(record)}
                />
            </div>
        );
    }
}

export const CRMListView = withModelManager(CRMListViewC);
