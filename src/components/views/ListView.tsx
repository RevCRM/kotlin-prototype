
import * as React from 'react';
import * as PropTypes from 'prop-types';

import Button from 'material-ui/Button';
import Add from 'material-ui-icons/Add';
import { ModelList } from 'rev-forms-materialui/lib/lists/ModelList';
import { IViewContext } from './ViewManager';

export interface IListViewProps {
    model: string;
    fields: string[];
}

export class ListView extends React.Component<IListViewProps> {

    context: { viewContext: IViewContext };
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
                <ModelList model={this.props.model} fields={this.props.fields} />
            </div>
        );
    }
}
