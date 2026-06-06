interface MetricCardProps {
  icon: string;
  label: string;
  value: number | string;
  colorVariant: "violet" | "emerald" | "amber" | "red";
}

const variantStyles: Record<MetricCardProps["colorVariant"], { bg: string; text: string }> = {
  violet: { bg: "bg-violet-100", text: "text-violet-600" },
  emerald: { bg: "bg-emerald-100", text: "text-emerald-600" },
  amber: { bg: "bg-amber-100", text: "text-amber-600" },
  red: { bg: "bg-red-100", text: "text-red-600" },
};

const MetricCard = ({ icon, label, value, colorVariant }: MetricCardProps) => {
  const styles = variantStyles[colorVariant];

  return (
    <div className="bg-white p-8 rounded-2xl shadow-sm border border-slate-100 flex flex-col justify-between h-44">
      <div className={`w-12 h-12 rounded-full flex items-center justify-center ${styles.bg} ${styles.text}`}>
        <span className="material-symbols-outlined">{icon}</span>
      </div>

      <div>
        <p className="text-xs font-bold text-slate-400 uppercase tracking-wider mb-1">{label}</p>
        <h3 className={`text-4xl font-black ${colorVariant === "violet" ? "text-slate-900" : colorVariant === "emerald" ? "text-emerald-600" : colorVariant === "amber" ? "text-amber-600" : "text-red-600"}`}>
          {value}
        </h3>
      </div>
    </div>
  );
};

export default MetricCard;
