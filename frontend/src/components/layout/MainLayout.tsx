import type { ReactNode } from "react";
import Sidebar from "./Sidebar";
import TopBar from "./TopBar";

interface MainLayoutProps {
  children: ReactNode;
  searchPlaceholder?: string;
  onSearch?: (term: string) => void;
}

const MainLayout = ({ children, searchPlaceholder, onSearch }: MainLayoutProps) => {
  return (
    <div className="min-h-screen bg-slate-50">
      <Sidebar />
      <div className="lg:ml-72 min-h-screen flex flex-col">
        <TopBar searchPlaceholder={searchPlaceholder} onSearch={onSearch} />
        <div className="p-6 lg:p-10 flex-1">{children}</div>
      </div>
    </div>
  );
};

export default MainLayout;
