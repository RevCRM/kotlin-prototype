
import * as React from 'react';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import Grid from '@material-ui/core/Grid';

export type ColspanOptions = 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12;
export interface IPanelProps {
    colspanNarrow?: ColspanOptions;
    colspan?: ColspanOptions;
    colspanWide?: ColspanOptions;
    title?: string;
}

export const Panel: React.SFC<IPanelProps> = (props) => {

    const gridProps = {
        xs: props.colspanNarrow || 12,
        md: props.colspan || 6,
        lg: props.colspanWide || props.colspan || 6
    };

    return (
        <Grid item {...gridProps}>
            <Card>
                {props.title && <CardHeader title={props.title} />}
                <CardContent>
                    <Grid container spacing={8}>
                        {props.children}
                    </Grid>
                </CardContent>
            </Card>
        </Grid>
    );
};
