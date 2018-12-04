import * as React from 'react';
import { Typography, Grid, Card, CardHeader, CardContent } from '@material-ui/core';
import { Bar } from 'britecharts-react';
import withResponsiveness from 'britecharts-react/lib/esm/helpers/withResponsiveness';

const ResponsiveBarChart = withResponsiveness(Bar);

const oppoData = [
    {
        value: 5,
        name: 'New Lead'
    },
    {
        value: 10,
        name: 'Negotiating'
    },
    {
        value: 6,
        name: 'Won'
    }
];

export const Dashboard = () => (
    <div style={{ margin: 16 }}>
        <Grid container spacing={16}>
            <Grid item xs={12}>
                <Typography variant="h4">My Dashboard</Typography>
            </Grid>
            <Grid item xs={12} md={6}>
                <Card>
                    <CardHeader title="Opportunities by Stage" />
                    <CardContent>
                        <ResponsiveBarChart
                            data={oppoData}
                            height={300}
                            isHorizontal={false}
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
