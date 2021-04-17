using Libraries.Db.Reposiitory.Interfaces;
using Microsoft.EntityFrameworkCore;
using System.Threading.Tasks;

namespace Libraries.Db.Reposiitory.Implementations
{
    public class Repostory<T> : IRepository<T> where T : class
    {
        protected DbContext Context;
        protected DbSet<T> Set { get; }

        public Repostory(DbContext context)
        {
            this.Context = context;
            this.Set = context.Set<T>();

        }

        DbContext IRepository<T>.Context => this.Context;

        async Task<T> IRepository<T>.AddAsync(T entity)
        {
            var res = await this.Set.AddAsync(entity);

            await this.Context.SaveChangesAsync();
            return res.Entity; 
        }
    }
}
