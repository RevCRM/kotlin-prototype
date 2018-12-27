import * as React from "react"
import { Typography, Grid, Card, CardHeader, TableHead, Table, TableCell, Checkbox, TableBody, TableRow, IconButton, Icon } from "@material-ui/core"
import { Bar } from "britecharts-react"
import withResponsiveness from "britecharts-react/lib/esm/helpers/withResponsiveness"
import { colors } from "britecharts"

const ResponsiveBarChart = withResponsiveness(Bar)

const oppoData = [
    {
        value: 5000000,
        name: "New Lead"
    },
    {
        value: 10000000,
        name: "Qualified"
    },
    {
        value: 8500000,
        name: "Negotiating"
    },
]

export const Dashboard: React.SFC = () => (
    <div style={{ margin: 16 }}>
        <Grid container spacing={16}>
            <Grid item xs={12}>
                <Typography variant="h4">My Dashboard</Typography>
            </Grid>
            <Grid item xs={12} md={6}>
                <Card>
                    <CardHeader
                        title="Open Opportunities by Stage"
                        action={
                            <IconButton>
                                <Icon>more_vert</Icon>
                            </IconButton>
                        }
                    />
                    <ResponsiveBarChart
                        data={oppoData}
                        height={300}
                        enableLabels={true}
                        labelsNumberFormat=","
                        isHorizontal={false}
                        colorSchema={colors.colorSchemas.britecharts}
                        margin={{ left: 90, right: 20, top: 20, bottom: 40 }}
                    />
                </Card>
            </Grid>
            <Grid item xs={12} md={6}>
                <Card>
                    <CardHeader
                        title="Todays Tasks"
                        action={
                            <IconButton>
                                <Icon>more_vert</Icon>
                            </IconButton>
                        }
                    />
                    <Table padding="none">
                        <TableHead>
                            <TableRow>
                                <TableCell />
                                <TableCell>
                                    Task
                                </TableCell>
                                <TableCell>
                                    Related To
                                </TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            <TableRow>
                                <TableCell padding="checkbox">
                                    <Checkbox />
                                </TableCell>
                                <TableCell>
                                    Prepare Proposal
                                </TableCell>
                                <TableCell>
                                    Opportunity 105: New Build
                                </TableCell>
                            </TableRow>
                            <TableRow>
                                <TableCell padding="checkbox">
                                    <Checkbox />
                                </TableCell>
                                <TableCell>
                                    Prepare Proposal
                                </TableCell>
                                <TableCell>
                                    Opportunity 107: Cable Street Works
                                </TableCell>
                            </TableRow>
                            <TableRow>
                                <TableCell padding="checkbox">
                                    <Checkbox />
                                </TableCell>
                                <TableCell>
                                    Buy Milk
                                </TableCell>
                                <TableCell>
                                    Personal
                                </TableCell>
                            </TableRow>
                        </TableBody>
                    </Table>
                </Card>
            </Grid>
        </Grid>
    </div>
)
