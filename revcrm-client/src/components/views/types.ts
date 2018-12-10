
/**
 * contextual information for child CRM views
 * e.g. default_account_id, event_type, etc.
 */
export interface IViewContext {
    model: string | null;
    modelId: number | null;
    [key: string]: any;
}
