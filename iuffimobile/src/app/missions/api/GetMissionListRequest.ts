export class GetMissionListRequest {
    public idMissione?: number | null;
    public numeroTrasferta?: number | null;
    public dallaData?: string | null;
    public allaData?: string | null;
    public idIspettoreAssegnato?: number | null;
    public cognomeIspettore?: string | null;
    public nomeIspettore?: string | null;
    public campionamento?: boolean | null;
    public trappolaggio?: boolean | null;
    public idSpecieVegetale?: number | null;
    public idOrganismoNocivo?: number | null;
    public stato?: string | null;

    constructor() {

    }
}
