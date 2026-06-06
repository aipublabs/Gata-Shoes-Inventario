import type { ReactNode } from "react";

const DataTable = ({ children }: { children: ReactNode }) => {
  return (
    <div className="overflow-x-auto rounded-3xl bg-white shadow-sm">
      <table className="min-w-full divide-y divide-surface-container-low">{children}</table>
    </div>
  );
};

export default DataTable;
