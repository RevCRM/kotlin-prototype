import * as React from 'react';
import { Typography, Grid, Card, CardHeader, CardContent } from '@material-ui/core';
import { Bar } from 'britecharts-react';

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
                        <Typography color="textSecondary" gutterBottom>
                        <Bar
                            data={oppoData}
                            height={400}
                            width={400}
                            isHorizontal={true}
                        />
                        </Typography>
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
