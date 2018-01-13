
import * as React from 'react';
import * as PropTypes from 'prop-types';

import Button from 'material-ui/Button';
import Add from 'material-ui-icons/Add';
import { IViewManagerContext, ListView } from 'rev-forms-materialui';

export interface ICRMListViewProps {
    model: string;
    fields: string[];
}

export class CRMListView extends React.Component<ICRMListViewProps> {

    context: IViewManagerContext;
    static contextTypes = {
        viewContext: PropTypes.object
    };

    render() {
        console.log('ListView props', this.props);
        console.log('ListView context', this.context);
        return (
            <div>
                <Button raised color="primary" style={{ marginBottom: 20 }}>
                    <Add style={{ marginRight: 10 }} />
                    New
                </Button>
                <ListView model={this.props.model} fields={this.props.fields} />
            </div>
        );
    }
}
