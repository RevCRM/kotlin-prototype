
import * as React from 'react';

import Button from 'material-ui/Button';
import Add from 'material-ui-icons/Add';
import { ModelList } from 'rev-forms-materialui/lib/lists/ModelList';

export interface IListViewProps {
    model: string;
    fields: string[];
    viewContext?: any;
}

export function ListView(props: IListViewProps) {
    console.log('ListView props', props);
    return (
        <div>
            <Button raised color="primary" style={{ marginBottom: 20 }}>
                <Add style={{ marginRight: 10 }} />
                New
            </Button>
            <ModelList model={props.model} fields={props.fields} />
        </div>
    );
}
