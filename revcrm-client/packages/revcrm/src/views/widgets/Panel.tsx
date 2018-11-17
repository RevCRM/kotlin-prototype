
import * as React from 'react';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import { IGridProps, Grid, GridItem } from './Grid';

export interface IPanelProps extends IGridProps {
    title?: string;
}

export const Panel: React.SFC<IPanelProps> = (props) => {

    return (
        <GridItem {...props} >
            <Card>
                {props.title && <CardHeader title={props.title} />}
                <CardContent>
                    <Grid>
                        {props.children}
                    </Grid>
                </CardContent>
            </Card>
        </GridItem>
    );
};
