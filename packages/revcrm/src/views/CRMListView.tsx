
import * as React from 'react';
import * as PropTypes from 'prop-types';

import Button from '@material-ui/core/Button';
import Add from '@material-ui/icons/Add';
import { ListView, IModelManagerProp, SearchView, SearchAction } from 'rev-ui';
import { ICRMViewManagerContext } from '../client/components/CRMViewManager';
import { IModel } from 'rev-models';
import { withModelManager } from 'rev-ui/lib/provider/withModelManager';
import Paper from '@material-ui/core/Paper';
import { withStyles, WithStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';

export interface ICRMListViewProps {
    searchFields?: React.ReactNode[];
    fields: string[];
    detailView?: string;
}

interface ICRMListViewState {
    where: object;
}

const styles = {
    searchWrapper: {
        marginTop: 20,
        padding: 24
    },
    listWrapper: {
        marginTop: 20
    }
};

class CRMListViewC extends React.Component<ICRMListViewProps & IModelManagerProp & WithStyles<keyof typeof styles>, ICRMListViewState> {

    context: ICRMViewManagerContext;
    static contextTypes = {
        viewContext: PropTypes.object
    };

    constructor(props: any, context: any) {
        super(props, context);
        this.state = {
            where: {}
        };
    }

    loadDetailView(args?: any) {
        if (!this.props.detailView) {
            throw new Error(`CRMListView onRecordClick() Error: no detailView set in view: ${this.context.viewContext.view.name}`);
        }
        const [ perspectiveName, viewName ] = this.props.detailView.split('/');
        this.context.viewContext.changePerspective(perspectiveName, viewName, args);
    }

    onSearch(newWhere: object) {
        this.setState({
            where: newWhere
        });
    }

    onItemPress(model: IModel) {
        const meta = this.props.modelManager.getModelMeta(model);
        const args = {
            [meta.primaryKey]: model[meta.primaryKey]
        };
        this.loadDetailView(args);
    }

    render() {
        console.log('ListView props', this.props);
        console.log('ListView context', this.context);
        const ctx = this.context.viewContext;
        return (
            <div>
                <Button variant="raised" color="primary"
                    onClick={() => this.loadDetailView()}
                >
                    <Add style={{ marginRight: 10 }} />
                    New
                </Button>

                {this.props.searchFields &&
                    <Paper className={this.props.classes.searchWrapper}>
                        <Typography variant="title" style={{ marginBottom: 12 }}>
                            Search
                        </Typography>

                        <SearchView
                            model={ctx.view.model}
                            onSearch={(where) => this.onSearch(where)}
                        >
                            {this.props.searchFields}

                            <div style={{ width: '100%', textAlign: 'right', paddingTop: 20 }}>
                                <SearchAction
                                    label="Search"
                                />
                            </div>
                        </SearchView>

                    </Paper>}

                <Paper className={this.props.classes.listWrapper}>
                    <ListView
                        model={ctx.view.model}
                        fields={this.props.fields}
                        where={this.state.where}
                        onItemPress={(record) => this.onItemPress(record)}
                    />
                </Paper>
            </div>
        );
    }
}

export const CRMListView = withStyles(styles)(withModelManager(CRMListViewC)) as any;
