
export interface IEntityMutationResult {
    result: any
    validation: {
        hasErrors: boolean
        entityErrors: [
            {
                entity: string
                entityPath: string
                code: string
                message: string
            }
        ]
        fieldErrors: [
            {
                entity: string
                fieldPath: string
                code: string
                message: string
            }
        ]
    }
}
