
/**
 * contextual information for child CRM views
 * e.g. default_account_id, event_type, etc.
 */
export interface IViewContext {
    entity?: string
    id?: string
    [key: string]: any
}
