
import * as React from 'react';
import * as PropTypes from 'prop-types';

import Button from 'material-ui/Button';
import Add from 'material-ui-icons/Add';
import { ListView } from 'rev-forms-materialui';
import { ICRMViewManagerContext } from './CRMViewManager';

export interface ICRMListViewProps {
    fields: string[];
}

export class CRMListView extends React.Component<ICRMListViewProps> {

    context: ICRMViewManagerContext;
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
                <ListView model={this.context.viewContext.model} fields={this.props.fields} />
            </div>
        );
    }
}
