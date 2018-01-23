
import * as React from 'react';
import * as PropTypes from 'prop-types';

import Button from 'material-ui/Button';
import Add from 'material-ui-icons/Add';
import { ListView, IModelManagerProp } from 'rev-ui';
import { ICRMViewManagerContext } from './CRMViewManager';
import { IModel } from 'rev-models';
import { withModelManager } from 'rev-ui/lib/provider/withModelManager';
import Paper from 'material-ui/Paper';
import { withStyles, WithStyles } from 'material-ui/styles';

export interface ICRMListViewProps {
    fields: string[];
    detailView?: string;
}

const styles = {
    listPaper: {
        marginTop: 20
    }
};

class CRMListViewC extends React.Component<ICRMListViewProps & IModelManagerProp & WithStyles<keyof typeof styles>> {

    context: ICRMViewManagerContext;
    static contextTypes = {
        viewContext: PropTypes.object
    };

    onRecordClick(model: IModel) {
        if (!this.props.detailView) {
            throw new Error(`CRMListView onRecordClick() Error: no detailView set in view: ${this.context.viewContext.view.name}`);
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
                <Button raised color="primary">
                    <Add style={{ marginRight: 10 }} />
                    New
                </Button>
                <Paper className={this.props.classes.listPaper}>
                    <ListView
                        model={ctx.view.model}
                        fields={this.props.fields}
                        onRecordPress={(record) => this.onRecordClick(record)}
                    />
                </Paper>
            </div>
        );
    }
}

export const CRMListView = withStyles(styles)(withModelManager(CRMListViewC));
