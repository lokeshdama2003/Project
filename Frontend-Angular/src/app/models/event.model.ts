export interface CommunityEvent {
  id?: number;
  title: string;
  category: string;
  date: string;
  venue: string;
  capacity: number;
  status: 'Planning' | 'Open' | 'Full' | 'Completed';
}
