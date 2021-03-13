using Citylogia.Server.Db.Implementations;
using Microsoft.AspNetCore.Builder;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
namespace Citylogia.Server.Db
{
    public static class Extensions
    {
        public static void MigrateDb(this IApplicationBuilder app)
        {
            var factory = app.ApplicationServices.GetRequiredService<IServiceScopeFactory>();

            using (var scope = factory.CreateScope())
            using (var context = scope.ServiceProvider.GetService<SqlContext>())
            {
                context.Database.Migrate();
            }
        }
    }
}
