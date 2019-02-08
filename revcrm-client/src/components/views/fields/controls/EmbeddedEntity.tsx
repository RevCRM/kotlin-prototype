
import * as React from "react"
import { IFieldComponentProps } from "./props"
import { IEntityMetadata, IFieldMetadata } from "../../../data/Metadata"
import { Grid } from "@material-ui/core"
import { getGridWidthProps } from "../../Grid"
import { EntityContext, IEntityContext } from "../../../data/EntityContext"

export const EmbeddedEntityControl =
    class extends React.Component<IFieldComponentProps> {
    entity: string
    entityMeta: IEntityMetadata
    embeddedEntityData: any

    constructor(props: any) {
        super(props)
        this.entity = this.props.field.constraints["Entity"]
        // TODO: This should not assume getEntity() returns an entity
        this.entityMeta = this.props.meta.getEntity(this.entity)!
        this.embeddedEntityData = this.props.value || {}
    }

    onFieldChange = (field: IFieldMetadata, value: any) => {
        Object.assign(this.embeddedEntityData, { [field.name]: value })
        this.props.onChange(this.embeddedEntityData)
    }

    render() {

        const { loadState, mode } = this.props.entity
        const dirtyFields: string[] = [] // TODO
        const gridWidthProps = getGridWidthProps(this.props)

        const entityContext: IEntityContext = {
            loadState,
            name: this.entity,
            meta: this.entityMeta,
            mode,
            data: this.embeddedEntityData,
            dirtyFields,
            onFieldChange: this.onFieldChange,
            save: this.props.entity.save
        }

        return (
            <EntityContext.Provider value={entityContext}>
                <Grid item {...gridWidthProps} container spacing={16}>
                    {this.props.children}
                </Grid>
            </EntityContext.Provider>
        )
    }
}
