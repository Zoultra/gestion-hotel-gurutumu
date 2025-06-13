export interface Menu{
   id?: string;
   titre?: string;
   icon?: string;
   url?: string;
   adminOnly?: boolean;
   sousMenu?: Array<Menu>
}