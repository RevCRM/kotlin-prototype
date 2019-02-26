
import * as React from "react"
import { IFieldComponentProps } from "./props"
import { IEntityMetadata, IFieldMetadata } from "../../../data/Metadata"
import { Field } from "../Field"
import { Button, Grid, Icon, IconButton, Table, TableBody, TableCell, TableHead, TableRow } from "@material-ui/core"
import { getGridWidthProps } from "../../Grid"
import { EntityContext, IEntityContext } from "../../../data/EntityContext"

export const EmbeddedEntityListControl =
    class extends React.Component<IFieldComponentProps> {
    entity: string
    entityMeta: IEntityMetadata
    entityListData: any[]
    listFieldMeta: IFieldMetadata[] = []
    listFieldComponents: React.ReactChild[]

    constructor(props: any) {
        super(props)
        this.entity = this.props.field.constraints["Entity"]
        // TODO: This should not assume getEntity() returns an entity
        this.entityMeta = this.props.meta.getEntity(this.entity)!
        this.entityListData = this.props.value || []

        this.listFieldComponents = React.Children.toArray(this.props.children)
            .filter(child => {
                if (typeof child == "object" && child.type == Field) {
                    const fieldMeta = this.entityMeta.fields.find(
                        f => f.name == child.props.name)
                    if (fieldMeta) {
                        this.listFieldMeta.push(fieldMeta)
                        return true
                    }
                }
                return false
            })
            .map((child => React.cloneElement(child as React.ReactElement<IFieldComponentProps>, {
                grid: null,
                label: false
            })))
    }

    onFieldChange = (rowIdx: number, field: IFieldMetadata, value: any) => {
        Object.assign(this.entityListData[rowIdx], { [field.name]: value })
        this.props.onChange(this.entityListData)
    }

    onAddRow = () => {
        this.entityListData.push({})
        this.props.onChange(this.entityListData)
        this.forceUpdate()
    }

    onDeleteRow = (rowIdx: number) => {
        this.entityListData.splice(rowIdx, 1)
        this.props.onChange(this.entityListData)
        this.forceUpdate()
    }

    render() {

        const { loadState, mode } = this.props.entity
        const dirtyFields: string[] = [] // TODO

        const control = (<>
            <Table padding="dense">
                <TableHead>
                    <TableRow>
                        {this.listFieldMeta.map(field => (
                            <TableCell key={field.name}>
                                {field.label}
                            </TableCell>
                        ))}
                        <TableCell />
                    </TableRow>
                </TableHead>
                <TableBody>
                    {this.entityListData.map((data, rowIdx) => {

                        const entityContext: IEntityContext = {
                            loadState,
                            name: this.entity,
                            meta: this.entityMeta,
                            mode,
                            data,
                            dirtyFields,
                            onFieldChange: this.onFieldChange.bind(this, rowIdx),
                            save: this.props.entity.save
                        }

                        return (
                            <EntityContext.Provider key={rowIdx} value={entityContext}>
                                <TableRow>
                                    {this.listFieldComponents.map((fieldComponent, cellIdx) => (
                                        <TableCell key={cellIdx}>
                                            {fieldComponent}
                                        </TableCell>
                                    ))}
                                    <TableCell>
                                        <IconButton onClick={this.onDeleteRow.bind(this, rowIdx)}>
                                            <Icon>delete</Icon>
                                        </IconButton>
                                    </TableCell>
                                </TableRow>
                            </EntityContext.Provider>
                        )

                    })}
                </TableBody>
            </Table>
            <Button
                variant="contained"
                onClick={this.onAddRow}
            >Add Row</Button>
        </>)

        if (this.props.grid) {
            const gridWidthProps = getGridWidthProps(this.props.grid)
            return (
                <Grid item {...gridWidthProps}>
                    {control}
                </Grid>
            )
        }
        else {
            return (
                <div>{control}</div>
            )
        }

    }
}
