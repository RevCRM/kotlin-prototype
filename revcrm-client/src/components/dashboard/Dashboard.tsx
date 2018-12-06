import * as React from 'react';
import { Typography, Grid, Card, CardHeader, CardContent } from '@material-ui/core';
import { Bar } from 'britecharts-react';
import withResponsiveness from 'britecharts-react/lib/esm/helpers/withResponsiveness';
import { colors } from 'britecharts';

const ResponsiveBarChart = withResponsiveness(Bar);

const oppoData = [
    {
        value: 5500,
        name: 'New Lead'
    },
    {
        value: 10000,
        name: 'Qualified'
    },
    {
        value: 8500,
        name: 'Negotiating'
    },
];

export const Dashboard = () => (
    <div style={{ margin: 16 }}>
        <Grid container spacing={16}>
            <Grid item xs={12}>
                <Typography variant="h4">My Dashboard</Typography>
            </Grid>
            <Grid item xs={12} md={6}>
                <Card>
                    <CardHeader title="Open Opportunities by Stage" />
                    <CardContent>
                        <ResponsiveBarChart
                            data={oppoData}
                            height={300}
                            enableLabels={true}
                            labelsNumberFormat=","
                            isHorizontal={false}
                            colorSchema={colors.colorSchemas.britecharts}
                        />
                    </CardContent>
                </Card>
            </Grid>
            <Grid item xs={12} md={6}>
                <Card>
                    <CardHeader title="Todays Tasks" />
                    <CardContent>
                        <Typography color="textSecondary" gutterBottom>
                        Task 1
                        </Typography>
                        <Typography color="textSecondary">
                        Task 2
                        </Typography>
                    </CardContent>
                </Card>
            </Grid>
        </Grid>
    </div>
);
