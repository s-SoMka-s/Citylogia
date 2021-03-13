using Citylogia.Server.Db.Implementations;
using Citylogia.Server.Tools.Interfaces.AppSettings.Types;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;

namespace Citylogia.Server.Db
{
    public static class Configurations
    {
        public static IServiceCollection AddDb(this IServiceCollection services, IConnectionStrings connectionStrings)
        {
            services.AddDbContext<SqlContext>(options => options.UseNpgsql(connectionStrings.Postgres), ServiceLifetime.Transient);

            return services;
        }
    }
}
