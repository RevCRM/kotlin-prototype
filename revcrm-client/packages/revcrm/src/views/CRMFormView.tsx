
import * as React from 'react';

import Grid from '@material-ui/core/Grid';
import Done from '@material-ui/icons/Done';
import Delete from '@material-ui/icons/Delete';
import { IDetailViewContextProp, DetailView, SaveAction, RemoveAction } from 'rev-ui';
import withStyles, { WithStyles } from '@material-ui/core/styles/withStyles';
import { CRMViewContext } from '../views/CRMViewManager';
import { withDetailViewContext } from 'rev-ui/lib/views/withDetailViewContext';

const styles = {
    toolbar: {
        display: 'flex',
        borderBottom: '1px solid #EBEBEB',
        '& button': {
            marginRight: 10
        }
    },
    formWrapper: {
        marginTop: 20
    }
};

class CRMFormViewContentC extends React.Component<IDetailViewContextProp & WithStyles<keyof typeof styles>> {

    render() {
        console.log('detail view context', this.props.detailViewContext);
        return (
            <Grid item xs={12}>
                <div className={this.props.classes.toolbar}>
                    <SaveAction
                        disabled={(ctx) => !ctx.dirty || ctx.loadState != 'NONE'}
                        onSuccess={(res) => console.log('SaveAction result', res)}
                        onError={(err: any) => {
                            console.log('SaveAction error', err);
                            console.log(err.validation);
                        }}
                    >
                        <Done style={{ marginRight: 10 }} />
                        Save
                    </SaveAction>
                    <RemoveAction
                        onSuccess={(res) => console.log(res)}
                        onError={(err) => console.log(err)}
                    >
                        <Delete style={{ marginRight: 10 }} />
                        Delete
                    </RemoveAction>
                </div>
                <Grid container spacing={8} className={this.props.classes.formWrapper}>
                    {this.props.children}
                </Grid>
            </Grid>
        );
    }
}

const CRMFormViewContent: React.ComponentType = withStyles(styles)(withDetailViewContext(CRMFormViewContentC));

export class CRMFormView extends React.Component {

    render() {
        return (
            <CRMViewContext.Consumer>{(ctx) => (
                <DetailView model={ctx.view.model} primaryKeyValue={ctx.primaryKeyValue} related={ctx.view.related}>
                    <CRMFormViewContent>{this.props.children}</CRMFormViewContent>
                </DetailView>
            )}</CRMViewContext.Consumer>
        );
    }
}
