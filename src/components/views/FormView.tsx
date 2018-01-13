
import * as React from 'react';
import * as PropTypes from 'prop-types';

import Button from 'material-ui/Button';
import { IViewContext } from './ViewManager';

export interface IFormViewProps {
    model: string;
}

export class FormView extends React.Component<IFormViewProps> {

    context: { viewContext: IViewContext };
    static contextTypes = {
        viewContext: PropTypes.object
    };

    render() {
        console.log('FormView props', this.props);
        console.log('FormView context', this.context);
        return (
            <div>
                <Button raised color="primary" style={{ marginBottom: 20 }}>
                    Save
                </Button>
                {this.context.viewContext.primaryKeyValue ? 'Edit ' : 'New '}
                {this.context.viewContext.modelMeta.name}
            </div>
        );
    }
}
