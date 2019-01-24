
import * as React from "react"
import { IFieldComponentProps } from "./props"
import { FormContext, IFormContext } from "../../FormView"
import { IEntityMetadata, IFieldMetadata } from "../../../meta/Metadata"
import { Field } from "../Field"
import { Grid, Table, TableBody, TableCell, TableHead, TableRow } from "@material-ui/core"
import { getGridWidthProps } from "../../Grid"

export const EmbeddedEntityListControl =
    class extends React.Component<IFieldComponentProps> {
    entity: string
    entityMeta: IEntityMetadata
    entityListData: any[]
    fields: IFieldMetadata[] = []
    fieldComponents: React.ReactChild[]

    constructor(props: any) {
        super(props)
        this.entity = this.props.field.constraints["Entity"]
        // TODO: This should not assume getEntity() returns an entity
        this.entityMeta = this.props.meta.getEntity(this.entity)!
        this.entityListData = this.props.value || []

        this.fieldComponents = React.Children.toArray(this.props.children)
            .filter(child => {
                if (typeof child == "object" && child.type == Field) {
                    const field = this.entityMeta.fields.find(
                        f => f.name == child.props.name)
                    if (field) {
                        this.fields.push(field)
                        return true
                    }
                }
                return false
            })
    }

    onFieldChange = (rowIdx: number, field: IFieldMetadata, value: any) => {
        Object.assign(this.entityListData[rowIdx], { [field.name]: value })
        this.props.onChange(this.entityListData)
    }

    render() {

        const { loadState, mode } = this.props.form
        const dirtyFields: string[] = [] // TODO
        const gridWidthProps = getGridWidthProps(this.props)

        return (
            <Grid item {...gridWidthProps}>
                <Table padding="dense">
                    <TableHead>
                        <TableRow>
                            {this.fields.map(field => (
                                <TableCell key={field.name}>
                                    {field.label}
                                </TableCell>
                            ))}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {this.entityListData.map((entityData, rowIdx) => {

                            const formContext: IFormContext = {
                                loadState,
                                entity: this.entity,
                                mode,
                                entityData,
                                dirtyFields,
                                onFieldChange: this.onFieldChange.bind(this, rowIdx),
                                save: this.props.form.save
                            }

                            return (
                                <FormContext.Provider key={rowIdx} value={formContext}>
                                    <TableRow>
                                        {this.fieldComponents.map((fieldComponent, cellIdx) => (
                                            <TableCell key={cellIdx}>
                                                {fieldComponent}
                                            </TableCell>
                                        ))}
                                    </TableRow>
                                </FormContext.Provider>
                            )

                        })}
                    </TableBody>
                </Table>
            </Grid>
        )

    }
}
