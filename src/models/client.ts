
import { ModelManager, InMemoryBackend } from 'rev-models';
import { UserLogin } from './User/UserLogin';
import { Company } from './Company/Company';
import { SelectionList } from './SelectionList/SelectionList';

export const clientModels = new ModelManager();
clientModels.registerBackend('default', new InMemoryBackend());

clientModels.register(UserLogin);
clientModels.register(Company);
clientModels.register(SelectionList);
