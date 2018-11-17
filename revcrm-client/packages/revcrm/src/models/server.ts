
import { User } from './User/User';
import { UserAuthData } from './User/backend/UserAuthData';
import { SelectionList } from './SelectionList/SelectionList';
import { RevCRMServer } from '../server';

export function registerModels(server: RevCRMServer) {
    server.models.register(User);
    server.models.register(UserAuthData);
    server.models.register(SelectionList);

    server.api.register(User);
    server.api.register(SelectionList);
}
