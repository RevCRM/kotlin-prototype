
import * as React from "react"
import { IFieldComponentProps } from "./props"
import { FormContext, IFormContext } from "../../FormView"
import { IEntityMetadata, IFieldMetadata } from "../../../meta/Metadata"
import { Field } from "../Field"
import { Grid } from "@material-ui/core"
import { getGridWidthProps } from "../../Grid"

export const EmbeddedEntityListControl =
    class extends React.Component<IFieldComponentProps> {
    entity: string
    entityMeta: IEntityMetadata
    entityListData: any[]
    fieldComponents: React.ReactChild[]
    fieldNames: string[] = []

    constructor(props: any) {
        super(props)
        this.entity = this.props.field.constraints["Entity"]
        // TODO: This should not assume getEntity() returns an entity
        this.entityMeta = this.props.meta.getEntity(this.entity)!
        this.entityListData = this.props.value || []

        this.fieldComponents = React.Children.toArray(this.props.children)
            .filter(child => {
                if (typeof child == "object" && child.type == Field) {
                    this.fieldNames.push(child.props.name)
                    return true
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

        return this.entityListData.map((entityData, rowIdx) => {

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
                <FormContext.Provider value={formContext}>
                    <Grid item {...gridWidthProps} container spacing={16}>
                        {this.fieldComponents}
                    </Grid>
                </FormContext.Provider>
            )

        })

    }
}
